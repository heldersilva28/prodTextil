using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using Microsoft.JSInterop;
using web.Models;
namespace web.Services;

public class AuthService
{
    private readonly HttpClient _http;
    private readonly IJSRuntime _jsRuntime;
    private const string AUTH_KEY = "auth_token";

    public AuthService(HttpClient http, IJSRuntime jsRuntime)
    {
        _http = http;
        _jsRuntime = jsRuntime;
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
        var result = await resp.Content.ReadFromJsonAsync<AuthResponse>();

        if (result.Success)
        {
            // Guardar informação de autenticação no session storage
            await _jsRuntime.InvokeVoidAsync("sessionStorage.setItem", AUTH_KEY, "authenticated");
        }

        return result;
    }

    public async Task LogoutAsync()
    {
        await _http.PostAsync("auth/logout", null);
        // Remover informação de autenticação do session storage
        await _jsRuntime.InvokeVoidAsync("sessionStorage.removeItem", AUTH_KEY);
    }

    public async Task<AuthResponse> RegisterClientAsync(ClientRequest dto)
    {
        var resp = await _http.PostAsJsonAsync("clientes", dto);
        // lê a resposta (status 200 ou 400/403)
        return await resp.Content.ReadFromJsonAsync<AuthResponse>();
    }

    public async Task<int> GetClientIdByEmailAsync(string email)
    {
        var response = await _http.GetAsync($"clientes/clientes/id?email={Uri.EscapeDataString(email)}");
        if (response.IsSuccessStatusCode)
        {
            return await response.Content.ReadFromJsonAsync<int>();
        }
        return 0; // or throw exception based on your error handling strategy
    }

    public async Task<bool> ExistemUtilizadoresAsync()
    {
        return await _http.GetFromJsonAsync<bool>("auth/existem-utilizadores");
    }

    public async Task<bool> GetCodigoPostalAsync(string codigo)
    {
        // Faz GET em "/{codigo}"
        var resp = await _http.GetAsync($"codigos-postais/{codigo}");
        // Lê a resposta (status 200 ou 400/403)
        /*if (resp.StatusCode == System.Net.HttpStatusCode.OK)
        {
            Console.WriteLine("Meti esta merda a true");
            return true;
        }
        else
        {
            // Se não for sucesso, retorna uma resposta de erro
            Console.WriteLine("Meti esta merda a false");
            return false;
        }*/
        return resp.IsSuccessStatusCode;
    }

    // Método para verificar se o utilizador está autenticado
    public async Task<bool> IsAuthenticatedAsync()
    {
        var token = await _jsRuntime.InvokeAsync<string>("sessionStorage.getItem", AUTH_KEY);
        return !string.IsNullOrEmpty(token);
    }
}
