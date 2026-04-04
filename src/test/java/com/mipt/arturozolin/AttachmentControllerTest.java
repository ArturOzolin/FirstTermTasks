package com.mipt.arturozolin;

import com.mipt.arturozolin.controller.AttachmentController;
import com.mipt.arturozolin.model.TaskAttachment;
import com.mipt.arturozolin.service.AttachmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AttachmentController.class)
class AttachmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttachmentService attachmentService;

    @Test
    void shouldUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        TaskAttachment mockAttachment = new TaskAttachment();
        mockAttachment.setId(1L);

        when(attachmentService.storeAttachment(eq(1L), any())).thenReturn(mockAttachment);
        mockMvc.perform(multipart("/api/tasks/1/attachments").file(file))
                .andExpect(status().isCreated());
    }
}