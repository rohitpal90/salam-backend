package com.salam.ftth.model;

import com.salam.ftth.model.request.IDType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserMetaInfo {
    private IDType idType;
    private String dob;
    private String nid;

    public UserMetaInfo(IDType idType, String dob, String nid) {
        this.idType = idType;
        this.dob = dob;
        this.nid = nid;
    }
}
