package oauth.signpost.commonshttp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import oauth.signpost.http.HttpRequest;
import oauth.signpost.mocks.OAuthProviderMock;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.ProtocolVersion;
import ch.boye.httpclientandroidlib.StatusLine;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpUriRequest;
import ch.boye.httpclientandroidlib.entity.InputStreamEntity;
import ch.boye.httpclientandroidlib.message.BasicStatusLine;
import org.mockito.Mockito;

@SuppressWarnings("serial")
public class CommonHttpOAuthProviderMock extends CommonsHttpOAuthProvider implements
        OAuthProviderMock {

    private HttpClient httpClientMock;

    public CommonHttpOAuthProviderMock(String requestTokenUrl, String accessTokenUrl,
            String websiteUrl) {
        super(requestTokenUrl, accessTokenUrl, websiteUrl);
    }

    @Override
    protected oauth.signpost.http.HttpResponse sendRequest(HttpRequest request) throws Exception {
        HttpResponse resp = httpClientMock.execute((HttpUriRequest) request.unwrap());
        return new HttpResponseAdapter(resp);
    }

    public void mockConnection(String responseBody) throws Exception {
        HttpResponse response = mock(HttpResponse.class);
        this.httpClientMock = mock(HttpClient.class);
        InputStream is = new ByteArrayInputStream(responseBody.getBytes());
        InputStreamEntity entity = new InputStreamEntity(is, responseBody.length());
        StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK");

        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(httpClientMock.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);
    }
}
