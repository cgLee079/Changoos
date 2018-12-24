package com.cglee079.changoos.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cglee079.changoos.constants.Path;
import com.cglee079.changoos.service.FileService;
import com.cglee079.changoos.util.MyFilenameUtils;

@Controller
public class FileController {
	@Autowired
	private FileService fileService;
	
	@Value("#{servletContext.getRealPath('/')}")
	private String realPath;
	
	@RequestMapping("/file/download.do")
	public void projectDoFiledownload(HttpServletRequest request, HttpServletResponse response,
			String path, String pathname, String filename) throws IOException {
		
		File file = new File(realPath + path, pathname);
		byte fileByte[] = FileUtils.readFileToByteArray(file);

		if (file.exists()) {
			response.setContentType("application/octet-stream");
			response.setContentLength(fileByte.length);
			response.setHeader("Content-Disposition", "attachment; fileName=\"" + MyFilenameUtils.encodeFilename(request, filename) + "\";");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.getOutputStream().write(fileByte);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
	
	@ResponseBody
	@RequestMapping("/mgnt/file/upload.do")
	public String projectImgUpload(Model model, MultipartFile file) throws IllegalStateException, IOException {
		String pathname= fileService.saveFile(file);
		
		JSONObject result = new JSONObject();
		result.put("path", Path.TEMP_FILE_PATH);
		result.put("pathname", pathname);
		
		return result.toString();
	}
}