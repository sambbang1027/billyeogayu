// 작성자 : 김민호
package app.domains.asset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AssetFilterOptionsDto {
    private final List<String> categories;
    private final List<String> companies;
    private final List<String> locations;
}
