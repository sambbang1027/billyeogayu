// 작성자 : 서샘이, 황요한
package app.domains.maintenance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString  @Builder
@Getter  @Setter
public class Maintenance {

	private int requestId;
	private String assetName;
	private String assetKind;
	private String company;
	private String parts;
	private String maintType;
	private String maintStatus;
	private String maintDate;
	private String adminName;
	private int rnum;
	private String createdAt;
	private String owner;
}
