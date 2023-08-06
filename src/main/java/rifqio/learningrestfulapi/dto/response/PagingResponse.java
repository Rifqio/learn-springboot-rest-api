package rifqio.learningrestfulapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PagingResponse {

    private Integer currentPage;

    private Integer totalPage;

    private Integer size;
}
