package main

import (
	"github.com/go-spring/spring-core/gs"
	_ "github.com/go-spring/starter-gin"
	"log"
	_ "paper-show/app"
)

func main() {
	err := gs.Run()
	if err != nil {
		log.Fatal(err)
	}
}
