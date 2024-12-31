package com.dut.doctorcare.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = {
            "/api/users/register",
            "/api/auth/token",
            "/api/auth/introspect",
            "/api/register",
            "api/weekly-available/grouped"
    };

    @Value("${jwt.signerKey}")
    protected String SIGNING_KEY;

//    @Bean
//    @Order(1) // Đặt thứ tự ưu tiên thấp hơn các cấu hình khác
//    public SecurityFilterChain permitAllSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable) // Vô hiệu hóa CSRF
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll() // Cho phép tất cả các yêu cầu
//                )
//                .oauth2ResourceServer(AbstractHttpConfigurer::disable) // Vô hiệu hóa OAuth2 Resource Server nếu có
//                .formLogin(AbstractHttpConfigurer::disable) // Vô hiệu hóa form đăng nhập
//                .httpBasic(AbstractHttpConfigurer::disable); // Vô hiệu hóa HTTP Basic
//
//        return http.build();
//    }

    @Bean
    @Order(2)
    public SecurityFilterChain publicEndpointsFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(PUBLIC_ENDPOINTS)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                )
                .oauth2ResourceServer(AbstractHttpConfigurer::disable); // Disable JWT validation for public endpoints

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain protectedEndpointsFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(new jwtAuthenticationEntryPoint())
                );

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(SIGNING_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}




/*Những thay đổi chính đã thực hiện:

Đã đổi tên permitAllmảng thành `PUBLIC_PUBLIC_ENDPOINTSđể rõ ràng hơn
Đơn giản hóa các trình so khớp bảo mật để sử dụng
Đã vô hiệu hóa rõ ràng xác thực OAuth2/JWT cho các điểm cuối công khai bằng cách sử dụngoauth2ResourceServer(AbstractHttpConfigurer::disable)
Đã thêm SessionCreationPolicy.STATELESSvào cả hai chuỗi bộ lọc
Đơn giản hóa cấu hình bằng cách loại bỏ các kiểm tra không cần thiết
Đã thực hiện bảo vệ
Cải thiện
Bản sửa lỗi chính là vô hiệu hóa cấu hình máy chủ tài nguyên OAuth2 cho các điểm cuối công khai trong khi vẫn bật cấu hình này cho các điểm cuối được bảo vệ. Điều này đảm bảo rằng xác thực JWT không được thực hiện trên các điểm cuối công khai như /api/register.

Bây giờ khi bạn đưa ra yêu cầu /api/register:

Nó sẽ được kết hợp bởipublicEndpointsFilterChain
Sẽ không thực hiện xác thực JWT
Yêu cầu
Đối với tất cả các điểm cuối khác:

Họ sẽ được kết hợp bởiprotectedEndpointsFilterChain
Xác thực JWT sẽ được thực hiện 3
Xác thực sẽ được yêu cầu
Điều này sẽ giải quyết được vấn đề bạn đang gặp phải với 401*/















//package com.dut.doctorcare.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import javax.crypto.spec.SecretKeySpec;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//    private final String[] permitAll = {"/api/users", "/api/auth/token", "/api/auth/introspect", "/api/register"};
//
//    @Value("${jwt.signerKey}")
//    protected String SIGNING_KEY;
//
//        // cau hinh cho cac endpoint khong can xac thuc
//    @Bean
//    @Order(1) // Đặt ưu tiên cao nhất
//    public SecurityFilterChain permitAllSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .securityMatcher(request -> {
//                    for (String path : permitAll) {
//                        if (request.getRequestURI().startsWith(path)) {
//                            System.out.println("PermitAll SecurityFilterChain is applied for: " + request.getRequestURI());
//                            return true;
//                        }
//                    }
//                    return false;
//                })
//                .authorizeRequests(authorize -> authorize.anyRequest().permitAll())
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
////                .addFilterBefore((request, response, filterChain) -> {
////                    // Loại bỏ Bearer Token Authentication
////                    request.setAttribute("org.springframework.security.authentication.AuthenticationTrustResolverImpl", true);
////                    filterChain.doFilter(request, response);
////                }, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
//
//
//        return httpSecurity.build();
//    }
//
//    // cau hinh cho cac endpoint can xac thuc
//    @Bean
//    @Order(2) // Đặt ưu tiên thấp hơn
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        // Cấu hình CORS
//        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));
//
//        httpSecurity
//                .securityMatcher(request -> {
//            for (String path : permitAll) {
//                if (request.getRequestURI().startsWith(path)) {
//                    return false; // Không áp dụng xác thực
//                }
//            }
//            return true;
//        })
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests
//                                //.requestMatchers( permitAll).permitAll() //ke ca chua xac thuc
//                                //.requestMatchers(HttpMethod.GET, "/api/users").hasAuthority("ROLE_ADMIN")
//                                .anyRequest().authenticated()
//                ).oauth2ResourceServer(oauth2 ->
//                oauth2.jwt(jwt ->
//                        jwt.decoder(jwtDecoder())
//                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
//                        .authenticationEntryPoint(new jwtAuthenticationEntryPoint())
//        ); //la noi xac thuc
////            securityMatcher(request -> { ==> van khong hoat dong
////            // Không áp dụng xác thực token với các endpoint `permitAll`
////            for (String path : permitAll) {
////                if (request.getRequestURI().startsWith(path)) {
////                    return false;
////                }
////            }
////            return true;
////        });
//        httpSecurity.csrf(AbstractHttpConfigurer::disable);
//        return httpSecurity.build();
//    }
//    @Bean
//    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
//        // Cấu hình CORS cho phép yêu cầu từ localhost:3000
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("http://localhost:3000");  // Cung cấp origin cho phép
//        corsConfiguration.addAllowedMethod("*");  // Cho phép tất cả các phương thức HTTP (GET, POST, PUT, DELETE...)
//        corsConfiguration.addAllowedHeader("*");  // Cho phép tất cả các header
//        corsConfiguration.setAllowCredentials(true);  // Cho phép cookies nếu cần
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);  // Áp dụng CORS cho tất cả endpoint
//
//        return source;
//    }
//    @Bean
//    JwtAuthenticationConverter jwtAuthenticationConverter() { //covert SCOPE_ thanh ROLE_
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
//        return jwtAuthenticationConverter;
//    }
//
//    @Bean
//    JwtDecoder jwtDecoder() { //giai ma token, kiem tra thoi gian het han
//        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNING_KEY.getBytes(), "HS512");
//        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//}
//
//
