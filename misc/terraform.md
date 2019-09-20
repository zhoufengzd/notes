# terraform notes
* spawning versus managing: create / destroy infrastructure
* containers: infrastructure as code

## configuration
* Hashicorp Configuration Language (HCL)

## constrains:
* destroy: leftover / or synchronization
* deploy: boot sequence
* refactor / rename
* version control by multiple developers
* not everything fits into terraform, like db


## Component:
### changes:
```
terraform plan
terraform apply
terraform show
terraform destroy
```

### workspace
```
terraform workspace new dev
terraform workspace new test
terraform workspace new prod
terraform workspace select dev
terraform workspace select default
terraform workspace select prod
```

### modules: a set of configuration files
* main.tf: provider + resources
* variables.tf
* outputs.tf

### state:
```
terraform {
  backend "gcs" {
    bucket = "my-terraform-states"
    prefix = "state-file-prefix"
  }
}
```

### variable
#### define & reference variable
```
variable "zone" {
  default = "europe-west1-a"
}

resource "google_compute_disk" "my-disk" {
  name = "my-disk"
  type = "pd-standard"
  zone = "${var.zone}"
}
```

#### update variable values
```
TF_VAR_zone=europe-west1-a terraform apply
terraform apply -var zone=europe-west1-a
terraform apply -var-file=production.tf
```

### resource
#### example: servers, databases, and load balancers.

#### resource = type + name + properties
```
resource "aws_instance" "example" {
    ami           = "ami-0c55b159cbfafe1f0"
    instance_type = "t2.micro"
    vpc_security_group_ids = [aws_security_group.instance.id]

    user_data = <<-EOF
              #!/bin/bash
              echo "Hello, World" > index.html
              nohup busybox httpd -f -p 8080 &
              EOF

    tags = {
        Name = "terraform-example"
    }
}

resource "aws_security_group" "instance" {
    name = "terraform-example-instance"

    ingress {
        from_port   = 8080
        to_port     = 8080
        protocol    = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }
}

resource "google_compute_disk" "my-disk" {
    name = "my-disk"
    type = "pd-standard"
    zone = "europe-west1-a"
}

terraform import google_compute_disk.my-disk my-disk
```

### data
```
data "google_compute_image" "my_image" {
  name    = "debian-9"
  project = "debian-cloud"
}

resource "google_compute_instance" "default" {
  #  …
  boot_disk {
    initialize_params {
      image = "${data.google_compute_image.my_image.self_link} "
    }
  }
}
```

### output values
```
terraform output
```

### lifecycle
```
disk {
  source_image = "debian-cloud/debian-9"
  disk_type    = "pd-standard"
  disk_size_gb = 20
  auto_delete  = true

  lifecycle {
    create_before_destroy = true
  }
}
```

## references:
* https://medium.com/driven-by-code/the-terrors-and-joys-of-terraform-88bbd1aa4359
* https://lzone.de/cheat-sheet/Terraform
