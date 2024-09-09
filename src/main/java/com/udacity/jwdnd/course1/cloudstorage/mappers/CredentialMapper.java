package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userid= #{userid}")
    List<Credential> getCredentials(Integer userid);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, #{username}, #{key}, #{password},#{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    void insertCredential(Credential credentials);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password= #{password} WHERE credentialid = #{credentialid}")
    void updateCredential(Credential credentials);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    int deleteCredential(int credentialid);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    Credential getCredentialById(Integer credentialid);
}
