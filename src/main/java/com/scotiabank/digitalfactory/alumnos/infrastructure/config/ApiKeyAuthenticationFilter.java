package com.scotiabank.digitalfactory.alumnos.infrastructure.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ApiKeyAuthenticationFilter implements WebFilter {
    private static final String HEADER_API_KEY = "X-API-KEY";

    private static final List<String> RUTAS_PUBLICAS = List.of("/actuator/health");

    private final String apiKey;

    public ApiKeyAuthenticationFilter(SecurityProperties securityProperties) {
        this.apiKey = securityProperties.apiKey();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (esRutaPublica(request.getPath().value())) {
            return chain.filter(exchange);
        }

        String apiKeyRecibida = request.getHeaders().getFirst(HEADER_API_KEY);

        if (apiKeyRecibida != null && apiKeyRecibida.equals(apiKey)) {
            var authentication = new UsernamePasswordAuthenticationToken(
                    "cliente-api", null, List.of());
            var securityContext = new SecurityContextImpl(authentication);

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
        }

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private boolean esRutaPublica(String path) {
        return RUTAS_PUBLICAS.contains(path);
    }
}
