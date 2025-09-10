package app.domains.assets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetPartsUpdateDto {
    private Asset asset;
    private List<Part> parts;
    private boolean assetCycleChanged;
    private boolean partCyclesChanged;
    private boolean partCountChanged;
}
