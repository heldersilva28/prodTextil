using Microsoft.AspNetCore.Components.Web;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;
using web;
using web.Services;

var builder = WebAssemblyHostBuilder.CreateDefault(args);
builder.RootComponents.Add<App>("#app");
builder.RootComponents.Add<HeadOutlet>("head::after");

builder.Services.AddScoped(sp =>
  new HttpClient { BaseAddress = new Uri("http://localhost:8080/api/") });

// Regista o serviço de autenticação
builder.Services.AddScoped<AuthService>();

await builder.Build().RunAsync();
