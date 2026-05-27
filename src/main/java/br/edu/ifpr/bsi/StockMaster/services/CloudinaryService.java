package br.edu.ifpr.bsi.StockMaster.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String upload(MultipartFile file) throws IOException {
        Map result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "stockmaster/logos",
                        "resource_type", "image"
                )
        );
        return (String) result.get("secure_url");
    }

    public void deletar(String url) throws IOException {
        if (url == null || !url.contains("cloudinary")) return;
        String publicId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        cloudinary.uploader().destroy("stockmaster/logos/" + publicId, ObjectUtils.emptyMap());
    }
}