package com.vennetics.bell.sam.terminal.status;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.vennetics.bell.sam.error.adapters.IErrorAdapter;
import com.vennetics.bell.sam.model.subscriber.status.SubscriberStatusResponse;

/**
 * The Test class for SmsController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
@WebIntegrationTest({ "server.port=0", "management.port=0", "spring.cloud.config.enabled=false"})
public class TerminalStatusControllerTest {

    private static final MediaType FORM_URLENCODED_MEDIA_TYPE = new MediaType(MediaType.APPLICATION_FORM_URLENCODED.getType(),
                                                        MediaType.APPLICATION_FORM_URLENCODED.getSubtype(), Charset.forName("utf8"));

    private static final MediaType XML_MEDIA_TYPE = new MediaType(MediaType.APPLICATION_XML.getType(),
            MediaType.APPLICATION_XML.getSubtype(), Charset.forName("utf8"));

    public static final String OUTBOUND_REQUEST_URL = TerminalStatusController.STATUS_URL.replace("{serviceIdentifier}", TestConfiguration.SERVICE);

    private MockMvc mockMvc;

    @Autowired
    @Qualifier("ss7ErrorAdapter")
    private IErrorAdapter errorAdapter;

    @Autowired
    @Qualifier("terminalStatusService")
    private ITerminalStatusService terminalStatusService;


    @Autowired
    private TerminalStatusController controller;

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    public void shouldAcceptSendStatusRequestFormUrlEncoded() throws Exception {

        final ResponseEntity<SubscriberStatusResponse> response = TestConfiguration.dummyResponse();

        when(terminalStatusService.sendStatusRequest(eq(TestConfiguration.SERVICE), any())).thenReturn(response);

        mockMvc.perform(get(OUTBOUND_REQUEST_URL)
                .accept(XML_MEDIA_TYPE)
                .contentType(FORM_URLENCODED_MEDIA_TYPE)
                .param(TestConfiguration.MSISDN_KEY, TestConfiguration.MSISDN)
                ).andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_XML));
    }

    @Test
    public void shouldDeliverErrorAdapter() throws Exception {
        assertThat(controller.getErrorAdapter(), is(errorAdapter));
    }

    @Test
    public void shouldDeliverExceptionClasses() throws Exception {
        final List<Class<?>> exceptionClasses = controller.exceptionClasses();
        assertThat(exceptionClasses, hasSize(0));
    }
}


