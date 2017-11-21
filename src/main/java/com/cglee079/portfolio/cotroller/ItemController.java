package com.cglee079.portfolio.cotroller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cglee079.portfolio.model.ItemVo;
import com.cglee079.portfolio.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;

	@RequestMapping(value = "/item/view")
	public String itemView(Model model, int seq){
		ItemVo item = itemService.get(seq);
		model.addAttribute("item", item);
		return "item_view";
	}
	
	@RequestMapping(value = "/admin/item")
	public String item(Model model) {
		List<ItemVo> items = itemService.list();
		model.addAttribute("items", items);
		return "item_list";
	}
	
	@RequestMapping(value = "/admin/item/delete.do")
	public String itemDelete(int seq) {
		boolean result = itemService.delete(seq);
		return "redirect:" + "/admin/item";
	}
	
	@RequestMapping(value = "/admin/item/upload", params = "!seq")
	public String itemUpload() {
		return "item_upload";
	}
	
	@RequestMapping(value = "/admin/item/upload", params = "seq")
	public String itemModify(Model model, int seq) {
		ItemVo item = itemService.get(seq);
		model.addAttribute("item", item);
		return "item_upload";
	}
	
	@RequestMapping(value = "/admin/item/upload.do", method = RequestMethod.POST, params = "!seq")
	public String itemDoUpload(HttpServletRequest request, ItemVo item, MultipartFile snapshtFile) throws IllegalStateException, IOException {
		HttpSession session = request.getSession();
		String rootPath = session.getServletContext().getRealPath("");
		String imgPath	= "/resources/image/snapshot/";
		String filename	= "snapshot_" + item.getName() + "_";
		
		if(snapshtFile.getSize() != 0){
			filename += snapshtFile.getOriginalFilename();
			File file = new File(rootPath + imgPath + filename);
			snapshtFile.transferTo(file);
			
			item.setSnapsht(imgPath + filename);
		} else{
			item.setSnapsht(imgPath + "default.jpg");
		}
		
		itemService.insert(item);
		
		return "redirect:" + "/admin/item";
	}
	
	@RequestMapping(value = "/admin/item/upload.do", method = RequestMethod.POST, params = "seq")
	public String itemDoModify(HttpServletRequest request, ItemVo item, MultipartFile snapshtFile) throws IllegalStateException, IOException{
		System.out.println("itemDoModify");
		
		HttpSession session = request.getSession();
		String rootPath = session.getServletContext().getRealPath("");
		String imgPath	= "/resources/image/snapshot/";
		String filename	= "snapshot_" + item.getName() + "_";
		
		if(snapshtFile.getSize() != 0){
			File existFile = new File (rootPath + item.getSnapsht());
			if(existFile.exists()){
				existFile.delete();
			}
			
			filename += snapshtFile.getOriginalFilename();
			File file = new File(rootPath + imgPath + filename);
			snapshtFile.transferTo(file);
			
			item.setSnapsht(imgPath + filename);
		} 
		
		itemService.update(item);
		
		return "redirect:" + "/admin/item";
	}
	
	@RequestMapping(value = "/admin/item/imgUpload.do", method = RequestMethod.POST)
	public String itemImgUpload(HttpServletRequest request, Model model,
			@RequestParam("upload")MultipartFile multiFile, String CKEditorFuncNum) throws IllegalStateException, IOException {
		HttpSession session = request.getSession();
		String rootPath = session.getServletContext().getRealPath("");
		String imgPath	= "/resources/image/contents/";
		String filename	= "content_" + new SimpleDateFormat("YYMMdd_HHmmss").format(new Date()) + "_";
		
		if(multiFile != null){
			filename += multiFile.getOriginalFilename();
			File file = new File(rootPath + imgPath + filename);
			multiFile.transferTo(file);
		}
		
		model.addAttribute("path", request.getContextPath() + imgPath + filename);
		model.addAttribute("CKEditorFuncNum", CKEditorFuncNum);
		
		return "item_imgupload";
	}

}
