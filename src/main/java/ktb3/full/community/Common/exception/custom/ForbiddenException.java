package ktb3.full.community.Common.exception.custom;

import ktb3.full.community.Common.exception.ErrorDetail;

import java.util.List;

public class ForbiddenException extends RuntimeException {
    private final List<ErrorDetail> errors;

    public ForbiddenException(List<ErrorDetail> errors) {
        super("forbidden");
        this.errors = errors;
    }
    public List<ErrorDetail> getErrors() {
        return errors;
    }
}
