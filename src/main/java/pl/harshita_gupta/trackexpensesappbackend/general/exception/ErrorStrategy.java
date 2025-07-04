package pl.harshita_gupta.trackexpensesappbackend.general.exception;

import java.util.List;

public interface ErrorStrategy {

    String returnExceptionMessage(String message);

    List<String> returnExceptionMessageList(List<String> messageList);

    String returnExceptionDescription(String description);

    List<String> returnExceptionDescriptionList(List<String> descriptionList);
}
