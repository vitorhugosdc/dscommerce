package com.vitor.dscommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ResourceServerConfig {

	@Value("${cors.origins}")
	private String corsOrigins;

	/*Filtro para o Spring Security não barrar o H2*/
	@Bean
	@Profile("test")
	/*Order é pra controlar qual executa antes de qual, no caso, esse executa primeiro que o de baixo*/
	@Order(1)
	public SecurityFilterChain h2SecurityFilterChain(HttpSecurity http) throws Exception {

		http.securityMatcher(PathRequest.toH2Console()).csrf(csrf -> csrf.disable())
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
		return http.build();
	}

	/*@Bean a gente define um componente por meio de um método, quando o tipo da classe não é um tipo NOSSO no qual temos acesso ao código fonte para colocar o @Component em cima dela
	 * Então estamos definindo um Componente SecurityFilterChain
	 * O Spring Security na versão atual dele você customiza ele definindo filtros*/
	@Bean
	@Order(3)
	public SecurityFilterChain rsSecurityFilterChain(HttpSecurity http) throws Exception {

		/*Acessando o .csrf que é um objeto HTTP do tipo HttpSecurity e chamando o csrf.disable
		 * Estamos desabilitando a proteção contra ataques do tipo csrf, que é um ataque quando se tem uma aplicação que grava dados na sessão,
		 * o que pode gerar uma vunerabilidade a ser explorada para acessar recursos de forma indevida
		 * Porém, como nosso back-end está liberando uma API REST e API REST não guarda estado em sessão, não precisamos nos preocupar com isso*/
		http.csrf(csrf -> csrf.disable());
		/*Aqui estamos chamando authorizeHttpRequests e configurar com uma função lambda para permitir qualquer requisição em qualquer caminho
		 * Existem várias outras permissões, como somente autorizados, somente com roles especificas, etc.
		 * Essa é uma configuração global das rotas
		 * Por padrão e preferência do Nélio, vamos permitir todas e as rotas que precisarem de restrição de acesso a gente configura elas a nível de ROTA para não ficar confuso a config global com de rotas*/
		http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
		http.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()));
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		return http.build();
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
		grantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		String[] origins = corsOrigins.split(",");

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOriginPatterns(Arrays.asList(origins));
		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}

	@Bean
	FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
				new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
}
