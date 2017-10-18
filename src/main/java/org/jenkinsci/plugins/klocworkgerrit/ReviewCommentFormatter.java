package org.jenkinsci.plugins.klocworkgerrit;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gerrit.extensions.api.changes.ReviewInput;
import org.jenkinsci.plugins.klocworkgerrit.data.converter.CustomIssueFormatter;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Issue;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Zheng XiuJue on 2017/9/29.
 */
public class ReviewCommentFormatter {
    private final String projectPath;
    private final String issueComment;
    private final String klocworkURL;

    public ReviewCommentFormatter(String projectPath, String issueComment, String klocworkURL) {
        this.projectPath = projectPath;
        this.issueComment = issueComment;
        this.klocworkURL = klocworkURL;
    }

    public void generateReviewComment(ReviewInput reviewInput, Multimap<String, Issue> finalIssues) {
        reviewInput.comments = new HashMap<String, List<ReviewInput.CommentInput>>();
        for (String file : finalIssues.keySet()) {
            reviewInput.comments.put(file, Lists.newArrayList(
                    Collections2.transform(finalIssues.get(file),
                            new Function<Issue, ReviewInput.CommentInput>() {
                                @Nullable
                                @Override
                                public ReviewInput.CommentInput apply(@Nullable Issue input) {
                                    if (input == null) {
                                        return null;
                                    }
                                    ReviewInput.CommentInput commentInput = new ReviewInput.CommentInput();
                                    commentInput.id = input.getId().toString();
                                    commentInput.line = input.getLine();
                                    commentInput.message = new CustomIssueFormatter(input, issueComment, klocworkURL).getMessage().replaceAll(projectPath, "");
                                    return commentInput;
                                }
                            }
                    )
                    )
            );
        }
    }
}
