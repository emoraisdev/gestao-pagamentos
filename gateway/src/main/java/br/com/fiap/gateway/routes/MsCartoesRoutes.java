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
public class MsCartoesRoutes {

    @Value("${api.mscartoes.server}")
    private String mscartoes;

    private static final String API_CARTAO = "/api/cartao";

    public void createRoutes(RouteLocatorBuilder.Builder routerBuilder, AuthenticationFilter authenticationFilter) {

        routerBuilder.route("cartao", r -> configureCartaoRoute(r, authenticationFilter));
    }

    private Buildable<Route> configureCartaoRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path(API_CARTAO)
                .and().method(HttpMethod.POST)
                .filters(f -> f.filter(authenticationFilter.apply(new CustomFilterConfig(""))))
                .uri(mscartoes);
    }

}
