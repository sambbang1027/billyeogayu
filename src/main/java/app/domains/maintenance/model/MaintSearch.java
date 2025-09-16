// 작성자 : 서샘이, 황요한
package app.domains.maintenance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class MaintSearch {

	 private String searchType;  // assetName, adminName
	    private String keyword;

	    private String assetKind;
		private String maintType;
		private String maintStatus;
		private String company;
	    private int startRow;       // 페이지네이션 시작
	    private int endRow;         // 페이지네이션 끝
	    
	    private String orderBy;  // 정렬 
	   

}
