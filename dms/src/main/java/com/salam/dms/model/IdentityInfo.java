package com.salam.dms.model;

import com.salam.libs.feign.elm.model.AddressDto;
import com.salam.libs.feign.elm.model.EntityDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@NoArgsConstructor
public class IdentityInfo {
    private String firstName;
    private String lastName;
    private String nationality;
    private String city;

    public IdentityInfo(EntityDto entityDto, List<AddressDto> addresses) {
        this.firstName = entityDto.getFirstName();
        this.lastName = entityDto.getLastName();
        this.nationality = entityDto.getNationality();

        if (!CollectionUtils.isEmpty(addresses)) {
            var address = addresses.get(0);
            this.city = address.getCity();
        }
    }
}
