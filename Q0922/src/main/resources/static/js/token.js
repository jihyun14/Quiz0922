export const TokenManager = (() => {
	const ACCESS_KEY = "accessToken";
	const REFRESH_KEY = "refeshToken";
	
	const getAccessToken = () => localStorage.getItem(ACCESS_KEY) || sessionStorage.getItem(ACCESS_KEY);
	const getRefreshToken = () => localStorage.getItem(REFRESH_KEY) || sessionStorage.getItem(REFRESH_KEY);
		
	const setTokens = (accessToken, refreshToken, rememberMe) => {
		if(rememberMe){
			localStorage.setItem(ACCESS_KEY, accessToken);
			localStorage.setItem(REFRESH_KEY, refreshToken);
		} else{
			sessionStorage.setItem(ACCESS_KEY, accessToken);
			sessionStorage.setItem(REFRESH_KEY, refreshToken);
		}
	};
	
	const updateAccessToken = (newToken) => {
		if(localStorage.getItem(REFRESH_KEY)){    // 저장소가 어딘지 체크하는 거임 
			localStorage.setItem(ACCESS_KEY, newToken);
		} else{
			sessionStorage.setItem(ACCESS_KEY, newToken);
		}
	};
	
	const clearTokens = () => {
		localStorage.removeItem(ACCESS_KEY);
	    localStorage.removeItem(REFRESH_KEY);
	    sessionStorage.removeItem(ACCESS_KEY);
	    sessionStorage.removeItem(REFRESH_KEY);
	};
	
	return {getAccessToken, getRefreshToken, setTokens, updateAccessToken, clearTokens};
}) ();
