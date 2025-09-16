// 작성자 : 서샘이
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
public class MaintDetail {

	private int requestId;
	private String resolverName;
	private String assetKind;
	private String assetName;
	private String type;
	private String parts;
	private String description;
	private String note;
	private String resolvedAt;
	private String owner;
}
