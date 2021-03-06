package cn.lfsenior.nsfw.user.action;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.lfsenior.core.action.BaseAction;
import cn.lfsenior.nsfw.user.entity.User;
import cn.lfsenior.nsfw.user.service.UserService;


public class UserAction extends BaseAction{
	@Resource
	private UserService userService;
	private List<User> userList;
	private User user;
	private File headImg;
	private String headImgFileName;
	private String headImgContentType;
	private File userExcel;
	private String userExcelFileName;
	private String userExcelContentType;
	
	/**
	 * 列表页面
	 * @return
	 */
	public String listUI(){
		userList=userService.findObjects();
		return "listUI";
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	public String addUI(){
		return "addUI";
	}
	
	/**
	 * 增加用户
	 * @return 重定向到listUI
	 */
	public String add(){
		try {
			if(user!=null){
				if(headImg!=null){
					String filePath = ServletActionContext.getServletContext().getRealPath("upload/user");
					String fileName=UUID.randomUUID().toString().replace("-", "")+headImgFileName.substring(headImgFileName.lastIndexOf("."));
					
					/*
					 * 保存文件
					 */
					FileUtils.copyFile(headImg, new File(filePath,fileName));
					
					/*
					 *设置文件路径 
					 */
					user.setHeadImg("user/"+fileName);
				}
				userService.save(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "list";
	}
	
	/**
	 * 编辑用户
	 * @return
	 */
	public String edit(){
		try {
			if(user!=null){
				if(headImg!=null){
					String filePath = ServletActionContext.getServletContext().getRealPath("upload/user");
					String fileName=UUID.randomUUID().toString().replace("-", "")+headImgFileName.substring(headImgFileName.lastIndexOf("."));
					
					/*
					 * 保存文件
					 */
					FileUtils.copyFile(headImg, new File(filePath,fileName));
					
					/*
					 *设置文件路径 
					 */
					user.setHeadImg("user/"+fileName);
				}
				userService.update(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "list";
	}
	
	/**
	 * 编辑页面
	 * @return
	 */
	public String editUI(){
		if(user!=null&&user.getId()!=null){
			user=userService.findObjectById(user.getId());
		}
		return "editUI";
	}
	
	/**
	 * 删除用户
	 * @return
	 */
	public String delete(){
		if(user!=null&&user.getId()!=null){
			userService.delete(user.getId());
		}
		return "list";
	}

	
	public String deleteSelected(){
		if(selectedRow!=null){
			for(String id:selectedRow){
				userService.delete(id);
			}
		}
		return "list";
	}
	
	/**
	 * 导出文件
	 */
	public String exportFile(){
		try {
			//获取所有文件
			userList=userService.findObjects();
			//导出
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/x-execl");
			response.setHeader("Content-Disposition", "attachment;filename="+new String("用户列表.xls".getBytes(),"ISO-8859-1"));
			ServletOutputStream outputStream = response.getOutputStream();
			userService.exportExcel(userList,outputStream);
			if(outputStream!=null){
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 导入文件
	 */
	public String importExcel(){
		if(userExcel!=null){
			//判断格式是否正确
			if(userExcelFileName.matches("^.+\\.(?i)((xls)|(xlsx))$")){
				userService.importExcel(userExcel,userExcelFileName);
			}
		}
		return "list";
	}
	
	
	
	/**
	 * 账号的唯一性验证
	 */
	public void verifyAccount(){
		//1、获取账号
		try {
			if(user!=null&&StringUtils.isNotEmpty(user.getAccount())){
				List<User> list=userService.findUserByAccountAndId(user.getId(),user.getAccount());
				String strResult="true";
				if(list!=null&&list.size()>0){
					/*
					 *说明该账号存在 
					 */
					strResult= "false";
				}
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setContentType("text/html");
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(strResult.getBytes());
				outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public String[] getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(String[] selectedRow) {
		this.selectedRow = selectedRow;
	}

	public File getHeadImg() {
		return headImg;
	}

	public void setHeadImg(File headImg) {
		this.headImg = headImg;
	}

	public String getHeadImgFileName() {
		return headImgFileName;
	}

	public void setHeadImgFileName(String headImgFileName) {
		this.headImgFileName = headImgFileName;
	}

	public String getHeadImgContentType() {
		return headImgContentType;
	}

	public void setHeadImgContentType(String headImgContentType) {
		this.headImgContentType = headImgContentType;
	}

	public File getUserExcel() {
		return userExcel;
	}

	public void setUserExcel(File userExcel) {
		this.userExcel = userExcel;
	}

	public String getUserExcelFileName() {
		return userExcelFileName;
	}

	public void setUserExcelFileName(String userExcelFileName) {
		this.userExcelFileName = userExcelFileName;
	}

	public String getUserExcelContentType() {
		return userExcelContentType;
	}

	public void setUserExcelContentType(String userExcelContentType) {
		this.userExcelContentType = userExcelContentType;
	}

	
}
