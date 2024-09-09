package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    @Autowired
    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentials(int userid){
        return credentialMapper.getCredentials(userid);
    }

    public void addCredentials(Credential credentials, int userId){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), encodedKey);

        Credential newCredentials = new Credential();
        newCredentials.setUrl(credentials.getUrl());
        newCredentials.setUsername(credentials.getUsername());
        newCredentials.setKey(encodedKey);
        newCredentials.setPassword(encryptedPassword);
        newCredentials.setUserid(userId);

        credentialMapper.insertCredential(newCredentials);
    }

    public int deleteCredentials(int credentialid){
        return credentialMapper.deleteCredential(credentialid);
    }

    public void editCredentials(Credential credentials){
        Credential storedCredential = credentialMapper.getCredentialById(credentials.getCredentialid());

        credentials.setKey(storedCredential.getKey());
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), credentials.getKey());
        credentials.setPassword(encryptedPassword);
        credentialMapper.updateCredential(credentials);
    }
}
