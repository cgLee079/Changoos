package com.cglee079.changoos.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cglee079.changoos.dao.BoardFileDao;
import com.cglee079.changoos.model.BoardFileVo;
import com.cglee079.changoos.util.MyFileUtils;
import com.cglee079.changoos.util.MyFilenameUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BoardFileService {
	@Autowired
	BoardFileDao boardFileDao;
	
	@Value("#{servletContext.getRealPath('/')}")
	private String realPath;
	
	@Value("#{location['temp.file.dir.url']}")
	private String tempDir;
	
	public String saveFile(MultipartFile multipartFile) throws IllegalStateException, IOException {
		String filename = MyFilenameUtils.sanitizeRealFilename(multipartFile.getOriginalFilename());
		String pathname = MyFilenameUtils.getRandomFilename(MyFilenameUtils.getExt(filename));
		long size = multipartFile.getSize();

		
		if (size > 0) {
			File file = new File(realPath + tempDir, pathname);
			multipartFile.transferTo(file);
		}

		return pathname;
	}
	
	public void insertFiles(String TB, String dir, int boardSeq, String fileValues) throws JsonParseException, JsonMappingException, IOException{
		MyFileUtils fileUtils = MyFileUtils.getInstance();
		
		List<BoardFileVo> files = new ObjectMapper().readValue(fileValues, new TypeReference<List<BoardFileVo>>(){});
		BoardFileVo file;
		
		for (int i = 0; i < files.size(); i++) {
			file = files.get(i);
			file.setBoardSeq(boardSeq);
			switch(file.getStatus()) {
			case "NEW" : //새롭게 추가된 파일
				if(boardFileDao.insert(TB, file)) {
					//임시폴더에서 본 폴더로 이동
					File existFile  = new File(realPath + tempDir, file.getPathname());
					File newFile	= new File(realPath + dir, file.getPathname());
					fileUtils.move(existFile, newFile);
				}
				break;
			case "REMOVE" : //기존에 있던 이미지 중, 삭제된 이미지
				if(boardFileDao.delete(TB, file.getSeq())) {
					fileUtils.delete(realPath + dir, file.getPathname());
				}
				break;
			}
		}
		
		fileUtils.emptyDir(realPath + tempDir);
	}

	public List<BoardFileVo> list(String fileTB, int seq) {
		return boardFileDao.list(fileTB, seq);
	}

}