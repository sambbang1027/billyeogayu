// 작성자 : 서샘이
package app.domains.maintenance.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter @Setter
public class MaintenanceApply {
		
		private int assetId;
		private List<MaintPart> parts;
		private int adminId;
		private String resolverName;
		private String type;
		private int requestId;
		
}
