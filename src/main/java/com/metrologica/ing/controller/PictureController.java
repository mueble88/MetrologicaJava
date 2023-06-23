package com.metrologica.ing.controller;

import com.metrologica.ing.message.ResponseMessage;
import com.metrologica.ing.model.Picture;
import com.metrologica.ing.service.PictureStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class PictureController {

    @Autowired
    private PictureStorageService pictureStorageService;

    @GetMapping("/pictures")
    private ResponseEntity<List<Picture>> allPictures(){
        List<Picture> images = pictureStorageService.getAllPictures();
        return ResponseEntity
                .ok()
                .body(images);
    }

    @GetMapping("/picture/{id}")
    private ResponseEntity<String> getPicture(@PathVariable long id) {
        Picture picture = pictureStorageService.getPicture(id);
//        String encoded = Base64Utils.encodeToString(picture.getImage());
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + picture.getName() + "\"")
//                .body(encoded);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(picture.getImage());
    }

/*
    @GetMapping("/download/{name}")
    private ResponseEntity<byte[]> getImage(@PathVariable String name){
        byte[] image = pictureStorageService.downloadImage(name);
        String encoded = Base64Utils.encodeToString(image);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(image);
    }
*/


    @PostMapping("/upload")
    private ResponseEntity<ResponseMessage> uploadPicture(@RequestParam("picture")MultipartFile file){
        String message = "";
        try{
            pictureStorageService.uploadImage(file);
//            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            message = "Uploaded the file successfully ";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception e){
//            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            message = "Could not upload the file!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }

    }


/*
    @GetMapping("/pictures")
    public ResponseEntity<List<ResponsePicture>> getListPicture(){

        List<ResponsePicture> pictures = pictureStorageService.getAllPictures().stream().map(picture -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/pictures")
                    .path(picture.getId())
                    .toUriString();

            return new ResponsePicture(
                    picture.getName(),
                    fileDownloadUri,
                    picture.getType(),
                    picture.getImage().length);
        }).collet(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(pictures);
    }

*/


}


