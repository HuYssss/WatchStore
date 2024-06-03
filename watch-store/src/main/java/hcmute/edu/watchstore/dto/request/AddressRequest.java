package hcmute.edu.watchstore.dto.request;

import hcmute.edu.watchstore.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String id;

    private String address;

    private String city;

    private String country;

    public AddressRequest(Address address) {
        this.id = address.getId().toHexString();
        this.address = address.getAddress();
        this.city = address.getCity();
        this.country = address.getCountry();
    }
}
