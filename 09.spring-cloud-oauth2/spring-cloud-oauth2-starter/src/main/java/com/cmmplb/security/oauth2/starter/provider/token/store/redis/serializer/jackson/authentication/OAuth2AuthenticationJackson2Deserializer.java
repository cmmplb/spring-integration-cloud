package com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.jackson.authentication;

import com.cmmplb.security.oauth2.starter.provider.converter.SimpleGrantedAuthority;
import com.cmmplb.security.oauth2.starter.provider.converter.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.MissingNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author penglibo
 * @date 2022-08-30 09:32:56
 * @since jdk 1.8
 * OAuth2Authentication反序列化
 * {@link org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Serializer}
 */

@Slf4j
public class OAuth2AuthenticationJackson2Deserializer extends StdDeserializer<OAuth2Authentication> {

    private static final long serialVersionUID = -8244662624061191438L;

    public static final String OAUTH_2_REQUEST = "oauth2Request";
    public static final String USER_AUTHENTICATION = "userAuthentication";
    public static final String DETAILS = "details";
    public static final String PRINCIPAL = "principal";
    public static final String CREDENTIALS = "credentials";
    public static final String REQUEST_PARAMETERS = "requestParameters";
    public static final String CLIENT_ID = "clientId";
    public static final String GRANT_TYPE = "grantType";
    public static final String REDIRECT_URI = "redirectUri";
    public static final String APPROVED = "approved";
    public static final String RESPONSE_TYPES = "responseTypes";
    public static final String SCOPE = "scope";
    public static final String RESOURCE_IDS = "resourceIds";
    public static final String EXTENSIONS = "extensions";
    public static final String AUTHORITIES = "authorities";

    public OAuth2AuthenticationJackson2Deserializer() {
        super(OAuth2Authentication.class);
    }

    @Override
    public OAuth2Authentication deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        JsonNode oauth2RequestNode = readJsonNode(jsonNode, OAUTH_2_REQUEST);
        JsonNode userAuthenticationNode = readJsonNode(jsonNode, USER_AUTHENTICATION);

        // 解析authentication
        Authentication authentication = parseAuthentication(mapper, userAuthenticationNode);
        // 解析OAuth2Request
        OAuth2Request request = parseOAuth2Request(mapper, oauth2RequestNode);

        OAuth2Authentication token = new OAuth2Authentication(request, authentication);

        JsonNode detailsNode = jsonNode.get(DETAILS);
        Object details = mapper.readValue(detailsNode.traverse(mapper), Object.class);
        token.setDetails(details);
        return token;
    }

    private Authentication parseAuthentication(ObjectMapper mapper, JsonNode jsonNode) throws JsonParseException, JsonMappingException, IOException {
        if (mapper == null || jsonNode == null) {
            return null;
        }
        User principal = parseOAuth2User(mapper, jsonNode.get(PRINCIPAL));
        Object credentials = readValue(mapper, jsonNode.get(CREDENTIALS), Object.class);
        Collection<? extends SimpleGrantedAuthority> grantedAuthorities = parseSimpleGrantedAuthorities(mapper, jsonNode.get("authorities"));

        return new UsernamePasswordAuthenticationToken(principal, credentials, grantedAuthorities);
    }

    private OAuth2Request parseOAuth2Request(ObjectMapper mapper, JsonNode json) {
        if (mapper == null || json == null) {
            return null;
        }
        Map<String, String> requestParameters = readValue(mapper, json.get(REQUEST_PARAMETERS), new TypeReference<Map<String, String>>() {
        });
        String clientId = readString(mapper, json.get(CLIENT_ID));
        String grantType = readString(mapper, json.get(GRANT_TYPE));
        String redirectUri = readString(mapper, json.get(REDIRECT_URI));
        Boolean approved = readValue(mapper, json.get(APPROVED), Boolean.class);
        Set<String> responseTypes = readValue(mapper, json.get(RESPONSE_TYPES), new TypeReference<Set<String>>() {
        });
        Set<String> scope = readValue(mapper, json.get(SCOPE), new TypeReference<Set<String>>() {
        });
        Set<String> resourceIds = readValue(mapper, json.get(RESOURCE_IDS), new TypeReference<Set<String>>() {
        });
        Map<String, Serializable> extensions = readValue(mapper, json.get(EXTENSIONS), new TypeReference<Map<String, Serializable>>() {
        });
        Collection<? extends SimpleGrantedAuthority> grantedAuthorities = parseSimpleGrantedAuthorities(mapper, json.get(AUTHORITIES));

        OAuth2Request request = new OAuth2Request(requestParameters, clientId, grantedAuthorities, approved, scope, resourceIds, redirectUri, responseTypes, extensions);
        TokenRequest tokenRequest = new TokenRequest(requestParameters, clientId, scope, grantType);
        request.refresh(tokenRequest);
        return request;
    }

    /**
     * 由于User没有构造函数，反序列化会失败，单独解析
     */
    private User parseOAuth2User(ObjectMapper mapper, JsonNode json) {
        if (mapper == null || json == null) {
            return null;
        }
        Long id = readValue(mapper, json.get(User.COLUMN_ID), Long.class);
        String username = readString(mapper, json.get(User.COLUMN_USERNAME));
        String password = readString(mapper, json.get(User.COLUMN_PASSWORD));
        Boolean accountNonExpired = readValue(mapper, json.get(User.COLUMN_ACCOUNT_NON_EXPIRED), Boolean.class);
        Boolean credentialsNonExpired = readValue(mapper, json.get(User.COLUMN_CREDENTIALS_NON_EXPIRED), Boolean.class);
        Boolean accountNonLocked = readValue(mapper, json.get(User.COLUMN_ACCOUNT_NON_LOCKED), Boolean.class);
        Collection<? extends SimpleGrantedAuthority> authorities = parseSimpleGrantedAuthorities(mapper, json.get(User.COLUMN_AUTHORITIES));

        return new User(id, username, password, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    private Collection<? extends SimpleGrantedAuthority> parseSimpleGrantedAuthorities(ObjectMapper mapper, JsonNode jsonNode) {
        return readValue(mapper, jsonNode, new TypeReference<Collection<? extends SimpleGrantedAuthority>>() {
        });
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

    private static String readString(ObjectMapper mapper, JsonNode jsonNode) {
        String s = readValue(mapper, jsonNode, String.class);
        return null == s ? "" : s;
    }


    private static <T> T readValue(ObjectMapper mapper, JsonNode jsonNode, Class<T> clazz) {
        if (mapper == null || jsonNode == null || clazz == null) {
            return null;
        }
        try {
            return mapper.readValue(jsonNode.traverse(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T readValue(ObjectMapper mapper, JsonNode jsonNode, TypeReference<T> type) {
        if (mapper == null || jsonNode == null || type == null) {
            return null;
        }
        try {
            return mapper.readValue(jsonNode.traverse(), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
