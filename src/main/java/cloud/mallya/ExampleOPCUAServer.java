package cloud.mallya;

import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExampleOPCUAServer {

    private static int PORT = 12686;

    static void main() {
        OpcUaServer server = create(PORT);
        try {
            server.startup().get(10, TimeUnit.SECONDS);
            System.out.println("Listening on opc.tcp://127.0.0.1:" + PORT + "/wiki, press Enter to stop.");
            System.in.read();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                server.shutdown().get(10, TimeUnit.SECONDS);
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                throw new RuntimeException(e);
            } finally {
                Stack.releaseSharedResources();
            }
        }
    }

    public static OpcUaServer create(int port) {
        EndpointConfig endpoint = EndpointConfig.newBuilder()
                .setBindAddress("127.0.0.1")
                .setHostname("127.0.0.1")
                .setBindPort(port)
                .setPath("/wiki")
                .setSecurityPolicy(SecurityPolicy.None)
                .setSecurityMode(MessageSecurityMode.None)
                .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS)
                .build();

        OpcUaServerConfig config = OpcUaServerConfig.builder()
                .setApplicationUri("urn:eclipse:milo:wiki:server")
                .setApplicationName(LocalizedText.english("Wiki server"))
                .setProductUri("urn:eclipse:milo:wiki")
                .setCertificateManager(new DefaultCertificateManager())
                .setEndpoints(Set.of(endpoint))
                .build();

        OpcUaServer server = new OpcUaServer(
                config,
                profile -> new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build())
        );

        return server;

    }
}
