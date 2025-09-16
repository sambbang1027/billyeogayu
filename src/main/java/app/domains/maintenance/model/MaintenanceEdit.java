// 작성자 : 서샘이
package app.domains.maintenance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter  @Setter
public class MaintenanceEdit {
		private String note;
		private int requestId;
}
