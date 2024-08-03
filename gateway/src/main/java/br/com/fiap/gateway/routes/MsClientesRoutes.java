package br.com.fiap.gateway.routes;

import br.com.fiap.gateway.security.AuthenticationFilter;
import br.com.fiap.gateway.security.CustomFilterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class MsClientesRoutes {

    @Value("${api.msclientes.server}")
    private String msclientes;

    private static final String API_CLIENTE = "/api/cliente";

    private static final String API_AUTENTICACAO = "/api/autenticacao";

    public void createRoutes(RouteLocatorBuilder.Builder routerBuilder, AuthenticationFilter authenticationFilter) {

        routerBuilder.route("cadastro", this::configureCadastroRoute)
                .route("login", this::configureLoginRoute)
                .route("listar", r -> configureListarClientesRoute(r, authenticationFilter));
    }

    private Buildable<Route> configureCadastroRoute(PredicateSpec r) {
        return r.path(API_CLIENTE)
                .and().method(HttpMethod.POST)
                .uri(msclientes);
    }

    private Buildable<Route> configureListarClientesRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path(API_CLIENTE)
                .and().method(HttpMethod.GET)
                .filters(f -> f.filter(authenticationFilter.apply(new CustomFilterConfig(""))))
                .uri(msclientes);
    }

    private Buildable<Route> configureLoginRoute(PredicateSpec r) {
        return r.path(API_AUTENTICACAO)
                .and().method(HttpMethod.POST)
                .uri(msclientes);
    }

}
