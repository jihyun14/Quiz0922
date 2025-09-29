import {TokenManager} from './token.js';

export async function tokenApi(url, options = {}){
	let accessToken = TokenManager.getAccessToken;
	let refreshToken = TokenManager.getRefreshToken;
	
	options.headers =  options.headers || {};
	if(accessToken) options.headers['Authorization'] = `Bearer ${accessToken}`;
	if(refreshToken) options.headers['Refresh-Token'] = refreshToken;
	
	let res = await fetch(url, options);
	
	// AccessToken 만료시 RefreshToken으로 자동 갱신 
	if(res.status === 401 && refreshToken){
		const refreshRes = await fetch("/api/auth/refresh", {
			method : "POST", 
			headers : {"Content-Type" : "application/json"},
			body: JSON.stringify({refreshToken})
		});
		
		
		if(!refreshRes.ok){
			TokenManager.clearTokens();
			throw new Error("Refresh Token 유효하지 않거나 만료되었습니다. 다시 로그인해주세요.");
		}
		
		const newTokens = await refreshRes.json();
		TokenManager.updateAccessToken(newTokens.accessToken);
		
		options.headers['Authorization'] = `Bearer ${newTokens.accessToken}`;
		res = await fetch(url, options);
	}
	
	if(!res.ok) throw new Error(`API 요청 실패 : ${res.status}`);
	return res.json();
}