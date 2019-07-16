package com.newsbhaskar.service;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsbhaskar.model.Address;
import com.newsbhaskar.model.Editor;
import com.newsbhaskar.repository.EditorRepository;

@Service
public class EditorServiceImpl implements EditorService {
	@Autowired EditorRepository editorRepo;
	ObjectMapper objectMapper = new ObjectMapper();
	@Autowired private JavaMailSender mailSender;
	SimpleMailMessage mailMessage=new SimpleMailMessage();
	
	@Override
	public void applyProfile(MultipartFile biodata, String jsondata) throws IOException, ParseException, java.text.ParseException {
		//Editor jsonData=objectMapper.readValue(jsondata, Editor.class);
		//objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject json=(org.json.simple.JSONObject) parser.parse(jsondata);
		
		String fullname=(String) json.get("fullname");
		String mobile=(String) json.get("mobile");
		String email=(String) json.get("email");
		String qualification=(String) json.get("qualification");
		String experience=(String) json.get("experience");
		String dob=(String) json.get("dob");
		System.out.println("date of birth is "+dob);
		
		String selfDescription=(String) json.get("selfDescription");
		
	    LocalDate localdob = LocalDate.parse(dob);
		//Address
		String state=(String) json.get("state");
		String city=(String) json.get("city");
		String landmark=(String) json.get("landmark");
		String area=(String) json.get("area");
		String pincode=(String) json.get("pincode");
		String addressline1=(String) json.get("addressline1");
		
		Editor editor=new Editor(biodata.getOriginalFilename(),biodata.getContentType(),biodata.getBytes(),
						fullname,
						mobile,
						email,
						experience,
						selfDescription,
						qualification,
						localdob
				);
		
		Address address=new Address(state,city,landmark,area,pincode,addressline1);
		address.setEditor(editor);
		editor.setAddress(address);
		editor.setStatus("tobeapproved");
		editor.setDoa(LocalDate.now());
		editorRepo.save(editor);
	}

	@Override
	public List<Editor> findAllNewApplicant(String args) {

		return editorRepo.findAllByStatus(args);
	}

	@Override
	public Editor findEditorById(Integer id) {
		return editorRepo.findById(id).orElse(null);
	}

	@Override
	public Editor getBiodata(Integer id) {
		return editorRepo.getBiodataById(id);
	}

	@Override
	public void sendAck(Integer id) {
		Editor editor=editorRepo.getOne(id);
		editor.setStatus("pending");

		mailMessage.setTo(editor.getEmail());
		mailMessage.setSubject("Interview Venue");
		mailMessage.setText("Mr. "+editor.getEmail()+" You are well informed that you have been selected for the interview on post for Editor. You're interview schedule will be held on "+LocalDate.now()+" "+"Please Do come with your all Academic & Identity Documents\n\n Thanks for choosing us...");
		try{
			mailSender.send(mailMessage);
			editorRepo.save(editor);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}

	}

	@Override
	public List<Editor> findAllApplicantAndEditor() {
		return editorRepo.findAll();
	}


	/*@Override
	public void applyProfile(Editor editor, Address address) {
		address.setEditor(editor);
		editor.setAddress(address);
		editorRepo.save(editor);
	}*/

	/*@Override
	public Editor getFile(Integer fileId) {
		return editorRepo.getOne(fileId);
	}*/
}
