package com.dnevi.healthcare.query.viewmodel;

import com.dnevi.healthcare.domain.model.conversation.InstantMessage;
import com.nsoft.chiwava.spring.pagination.Pagination;
import com.nsoft.chiwava.spring.pagination.results.ResultSet;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;


public class ViewModelMessageResultSetBuilder {
    private ViewModelMessageResultSetBuilder() {
    }

    public static ViewModelMessage buildOne(InstantMessage instantMessage) {
        var viewModelMessage = new ViewModelMessage();
        viewModelMessage.setId(instantMessage.getId());
        viewModelMessage.setSender(instantMessage.getSender().getEmail());
        viewModelMessage.setMessage(instantMessage.getPayload());
        viewModelMessage.setConversationId(instantMessage.getConversation().getId());
        viewModelMessage.setMessageType(instantMessage.getMessageType());
        viewModelMessage.setCreatedAt(instantMessage.getCreatedAt());

        return viewModelMessage;
    }

    public static ResultSet<ViewModelMessage> build(Page<InstantMessage> results) {
        Pagination pagination = Pagination.fromPage(results);
        var data = mapToPresentation(results);

        return new ResultSet<>(pagination, data);
    }

    private static List<ViewModelMessage> mapToPresentation(Page<InstantMessage> results) {
        return results.stream().map(ViewModelMessageResultSetBuilder::buildOne)
                .collect(Collectors.toList());
    }
}
