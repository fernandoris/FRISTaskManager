resource "docker_network" "tm_network" {
  name = "tm_network"
}

resource "docker_container" "fristaskmanagerbackend" {
  name = "fristaskmanagerbackend"
  depends_on = [docker_container.db]
  image      = "fernandoris/fris_task_manager_backend:0.0.1-SNAPSHOT"
  env        = [
    "DB_USER=${var.db_user}",
    "DB_PASS=${var.db_pass}"
  ]
  restart = "always"
  ports {
    internal = 8080
    external = 8080
  }
  ports {
    internal = 8081
    external = 8081
  }
  network_mode = docker_network.tm_network.name
}

resource "docker_container" "db" {
  name = "db"
  image   = "mysql"
  command = ["--default-authentication-plugin=caching_sha2_password"]
  env     = [
    "MYSQL_ROOT_PASSWORD=${var.db_root_pass}",
    "MYSQL_DATABASE=fristaskmanager",
    "MYSQL_USER=${var.db_user}",
    "MYSQL_PASSWORD=${var.db_pass}"
  ]
  restart = "always"
  ports {
    internal = 3306
    external = 3306
  }
  network_mode = docker_network.tm_network.name
}

resource "docker_container" "adminer" {
  name = "adminer"
  image   = "adminer"
  restart = "always"
  ports {
    internal = 8080
    external = 8082
  }
  network_mode = docker_network.tm_network.name
}
