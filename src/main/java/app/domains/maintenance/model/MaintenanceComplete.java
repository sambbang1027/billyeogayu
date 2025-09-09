package app.domains.maintenance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter  @Setter
public class MaintenanceComplete {
	private int requestId;
	private String description;
	private String note;
}
