package guru.springfamework.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorDTO {

    @ApiModelProperty(value = "This is the id of the vendor")
    private Long id;

    @ApiModelProperty(value = "This is the name of the vendor", required = true)
    private String name;

    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(value = "This is the url that you can use to find the vendor in this api",
            notes = "This is a readonly information")
    private String vendor_url;
}
