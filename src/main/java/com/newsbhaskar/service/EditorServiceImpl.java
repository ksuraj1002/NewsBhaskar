package com.newsbhaskar.service;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.newsbhaskar.repository.NewsRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	@Autowired NewsRepository newsRepository;

	SimpleMailMessage mailMessage=new SimpleMailMessage();
	Logger logger=LoggerFactory.getLogger(EditorServiceImpl.class);

	private String args;
	InvalidEmailFoundException invalidEmailFoundException=new InvalidEmailFoundException(args);

	@Override
	public void deleteEditorById(int id) {
		editorRepo.deleteById(id);
	}

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
		//address.setEditor(editor);
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

	@Override
	public int countsNewApplicant(String tobeapproved) {
		return editorRepo.countByStatus(tobeapproved);
	}

	@Override
	public void rejectProcess(Integer id, String reason,String status) throws IOException, ParseException, InvalidEmailFoundException {
		Editor editor=editorRepo.getOne(id);

		/*boolean isValid=false;
		try {
			InternetAddress internetAddress=new InternetAddress(editor.getEmail());
			internetAddress.validate();
			isValid=true;
			System.out.println(isValid);
		} catch (AddressException e) {
			System.out.println(" "+isValid+""+e.getMessage());
		} */
		validateEmail(editor.getEmail());

		mailMessage.setTo(editor.getEmail());
		mailMessage.setSubject("Termination Letter");
		mailMessage.setText(reason);
			logger.warn("Here I am in mail message");
			mailSender.send(mailMessage);
		editor.setReason(reason);
		editor.setStatus(status);
		editorRepo.save(editor);
	}

	@Override
	public List<Editor> findAllEditor() {
		return editorRepo.findAll();
	}

	private void validateEmail(String email) throws IOException, ParseException, InvalidEmailFoundException {
		JSONParser parser = new JSONParser();
		JSONObject json;

		String key = "1BZD2K7XGXJKWX4NT5F3";
		Hashtable<String, String> data = new Hashtable<String, String>();
		data.put("format", "json");
		data.put("email", email);

		String datastr = "";
		for (Map.Entry<String, String> entry : data.entrySet()) {
			datastr += "&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
		}
		URL url = new URL("https://api.mailboxvalidator.com/v1/validation/single?key=" + key + datastr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output,out="";

		while ((output = br.readLine()) != null) {
			out+=output;
		}
		json=(JSONObject) parser.parse(out);
		System.out.println(json.get("is_verified"));

		if(!json.get("is_verified").toString().equalsIgnoreCase("true")){
			throw new InvalidEmailFoundException("Email does not exist");
		}

		conn.disconnect();

	}


	@Override
	public long countsAllNews() {
		return newsRepository.count();
	}

	@Override
	public int countsExistingEditor(String existing) {
		return editorRepo.countByStatus(existing);
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
