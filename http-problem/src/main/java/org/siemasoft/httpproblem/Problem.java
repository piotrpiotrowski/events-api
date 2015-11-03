package org.siemasoft.httpproblem;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-http-problem-06">draft-nottingham-http-problem-06</a>
 */
@Data
@ToString(exclude = "details")
public class Problem {

    private String code;

    private String message;

    private List<ProblemDetail> details = new ArrayList<>();
}
