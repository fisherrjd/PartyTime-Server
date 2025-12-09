{ pkgs ? import
    (fetchTarball {
      name = "jpetrucciani-2025-12-08";
      url = "https://github.com/jpetrucciani/nix/archive/cc1bf2e80a4bf4db9d3697bd40002ff38735aa31.tar.gz";
      sha256 = "1x1pr2lb84z8s5615gv16ibvrd8j4kiibgxzgq39x5fy02bdl1cd";
    })
    { }
}:
let
  name = "PartyTime-Server";
  uvEnv = pkgs.uv-nix.mkEnv {
    inherit name; python = pkgs.python313;
    workspaceRoot = pkgs.hax.filterSrc { path = ./.; };
    pyprojectOverrides = final: prev: { };
  };

  tools = with pkgs; {
    cli = [
      jfmt
      nixup
    ];
    uv = [ uv uvEnv ];
    scripts = pkgs.lib.attrsets.attrValues scripts;
  };

  scripts = with pkgs; { };
  paths = pkgs.lib.flatten [ (builtins.attrValues tools) ];
  env = pkgs.buildEnv {
    inherit name paths; buildInputs = paths;
  };
in
(env.overrideAttrs (_: {
  inherit name;
  NIXUP = "0.0.10";
} // uvEnv.uvEnvVars)) // { inherit scripts; }
