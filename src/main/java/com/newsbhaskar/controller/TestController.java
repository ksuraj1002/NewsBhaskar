package com.newsbhaskar.controller;

import com.newsbhaskar.model.*;
import com.newsbhaskar.repository.BookRepository;
import com.newsbhaskar.repository.PlantsRepository;
import com.newsbhaskar.service.EditorService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class TestController {
    @Autowired
    EditorService editorService;

    @Autowired
    PlantsRepository plantsRepository;
    @Autowired
    BookRepository bookRepository;

    @GetMapping("/getjsons")
    @ResponseBody
    public List<Plants> getJsonData() throws IOException, ParseException {

        URL url=new URL("http://plantplaces.com/perl/mobile/viewplantsjson.pl?Combined_Name=Oak");
        URLConnection resource=url.openConnection();
        InputStream stream=resource.getInputStream();
        String line=null,link="";
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream))) {
            while((line=bufferedReader.readLine())!=null){
                link+=line;
            }
        }

        org.json.JSONObject root=new org.json.JSONObject(link);
        JSONArray plantsArray=root.getJSONArray("plants");

        Plants plants1=new Plants();
        List<Plants> listOfPlant=new ArrayList<Plants>();
        for(int i=0; i<plantsArray.length(); i++){
            Plants plants=new Plants();
            org.json.JSONObject plantObj=plantsArray.getJSONObject(i);

            plants.setId(plantObj.getInt("id"));
            plants.setSpecies(plantObj.getString("species"));
            plants.setGenus(plantObj.getString("genus"));
            plants.setCultivar(plantObj.getString("cultivar"));
            plants.setCommon(plantObj.getString("common"));

            listOfPlant.add(plants);

        }
        plantsRepository.saveAll(listOfPlant);
        return listOfPlant;
    }

    @GetMapping("/saveauthority")
    @ResponseBody
    public String saveAuthorityData() throws IOException {
        StringBuilder builder=new StringBuilder();
        URL url=new URL("https://api.data.gov.in/resource/8d308158-28aa-4bcf-9e98-948b029340ad?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=xml&offset=0&limit=10");
        URLConnection resource=null;
        try{
            resource=url.openConnection();
            resource.connect();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        InputStream inputStream=null;
       try{
         inputStream=resource.getInputStream();
       }catch (Exception e){
           System.out.println("here "+e.getMessage());
       }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        BufferedReader reader=new BufferedReader(new InputStreamReader(bufferedInputStream));
        // read one line at a time.
        String inputLine = reader.readLine();
        while (inputLine != null) {
            // add this to our output
            builder.append(inputLine);
            // reading the next line
            inputLine = reader.readLine();
        }
        System.out.println(inputLine);
        JSONObject root=new JSONObject();
        return "";
    }

/*    @GetMapping("/test")
    public String returnTest() {
        return "index1";
    }*/

    @GetMapping("/savelist")
    @ResponseBody
    public String saveList(){
        List<Books> books=new ArrayList<Books>();

     for(int i=0; i<4; i++){
         Books bookish=new Books();
        bookish.setBooks("facts"+i);
         books.add(bookish);
     }


       System.out.println(books.size());

        bookRepository.saveAll(books);
        return "list save successfully";
    }



    @PostMapping("/submittest")
    @ResponseBody
    public Thenga setData(Thenga thenga){
        System.out.println(thenga.getName());
        System.out.println(thenga.getCourse());
        System.out.println(thenga.getMobile());
        System.out.println(thenga.getEmail());
        return thenga;
    }

    @GetMapping("/getDetails")
    @ResponseBody
    public List<Editor> getDetail(){
        return editorService.findAllEditor();
    }

    @GetMapping("/test")
    public String getTest(Model model) {
      /*  model.addAttribute("news", newsRepo.getOne(20));*/
        return "index1";
    }

    @PostMapping("/uploadmultiplefile")
    @ResponseBody
    public String postUploadFile(Resume resume){
        System.out.println(resume.getProfilephotoFile().getOriginalFilename()+" "+resume.getProfilephotoFile().getContentType());
        System.out.println(resume.getResumeFile().getOriginalFilename()+" "+resume.getResumeFile().getContentType());
        return "uploaded successfull";
    }


}
