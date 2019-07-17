package com.newsbhaskar.service;

import java.io.IOException;
import java.util.List;

import com.newsbhaskar.model.Editor;
import org.json.simple.parser.ParseException;
import org.springframework.web.multipart.MultipartFile;
public interface EditorService {

	void applyProfile(MultipartFile biodata, String jsondata) throws IOException, ParseException, java.text.ParseException;

    List<Editor> findAllNewApplicant(String args);

    Editor findEditorById(Integer id);

    Editor getBiodata(Integer id);

    void sendAck(Integer id);

    List<Editor> findAllApplicantAndEditor();

    int countsNewApplicant(String tobeapproved);

    int countsExistingEditor(String existing);

    long countsAllNews();

    void rejectProcess(Integer id, String reason,String status) throws IOException, ParseException, InvalidEmailFoundException;
}
