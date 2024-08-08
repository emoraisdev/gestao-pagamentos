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
public class MsPagamentosRoutes {

    @Value("${api.mspagamentos.server}")
    private String msPagamentos;

    private static final String API_PAGAMENTOS = "/api/pagamentos";

    private static final String API_PAGAMENTOS_CLIENTE = API_PAGAMENTOS + "/cliente/**";

    public void createRoutes(RouteLocatorBuilder.Builder routerBuilder, AuthenticationFilter authenticationFilter) {

        routerBuilder.route("pagamentos", r -> configurePagamentosRoute(r, authenticationFilter))
                .route("pagamentosClientes", r -> configurePagamentosPorClienteRoute(r, authenticationFilter));
    }


    private Buildable<Route> configurePagamentosRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path(API_PAGAMENTOS)
                .and().method(HttpMethod.POST)
                .filters(f -> f.filter(authenticationFilter.apply(new CustomFilterConfig(""))))
                .uri(msPagamentos);
    }

    private Buildable<Route> configurePagamentosPorClienteRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path(API_PAGAMENTOS_CLIENTE)
                .and().method(HttpMethod.GET)
                .filters(f -> f.filter(authenticationFilter.apply(new CustomFilterConfig(""))))
                .uri(msPagamentos);
    }
}
