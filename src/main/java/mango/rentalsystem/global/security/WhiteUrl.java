package mango.rentalsystem.global.security;

import java.util.List;

public class WhiteUrl {

	public static final List<String> FILTER_WHITE_LIST = List.of("/auth/login", "/auth/reissue");
}
