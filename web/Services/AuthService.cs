using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using web.Models;
using Web.Models;

namespace web.Services;

public class AuthService
{
    private readonly HttpClient _http;

    public AuthService(HttpClient http)
    {
        _http = http;
    }

    public async Task<AuthResponse> RegisterAsync(RegisterRequest dto)
    {
        // força TipoUtilizadorId = 4 no registo de utilizadores normais
        dto.TipoUtilizadorId = 4;
        var resp = await _http.PostAsJsonAsync("auth/register", dto);
        // lê a resposta (status 200 ou 400/403)
        return await resp.Content.ReadFromJsonAsync<AuthResponse>();
    }

    public async Task<AuthResponse> LoginAsync(LoginRequest dto)
    {
        var resp = await _http.PostAsJsonAsync("auth/login", dto);
        return await resp.Content.ReadFromJsonAsync<AuthResponse>();
    }

    public async Task<bool> ExistemUtilizadoresAsync()
    {
        return await _http.GetFromJsonAsync<bool>("auth/existem-utilizadores");
    }
}
