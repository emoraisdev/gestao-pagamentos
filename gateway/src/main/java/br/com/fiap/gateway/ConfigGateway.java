package br.com.fiap.gateway;

import br.com.fiap.gateway.routes.MsCartoesRoutes;
import br.com.fiap.gateway.routes.MsClientesRoutes;
import br.com.fiap.gateway.routes.MsPagamentosRoutes;
import br.com.fiap.gateway.security.AuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ConfigGateway {

    private final AuthenticationFilter authenticationFilter;

    private final MsClientesRoutes clientesRoutes;
    private final MsCartoesRoutes cartoesRoutes;

    private final MsPagamentosRoutes pagamentosRoutes;


    @Bean
    public RouteLocator custom(RouteLocatorBuilder builder){

        var routerBuilder = builder.routes();

        // Cada serviço terá a própria implementação da configuração de rotas.
        clientesRoutes.createRoutes(routerBuilder, authenticationFilter);
        cartoesRoutes.createRoutes(routerBuilder, authenticationFilter);
        pagamentosRoutes.createRoutes(routerBuilder, authenticationFilter);

        return routerBuilder.build();
    }

}
