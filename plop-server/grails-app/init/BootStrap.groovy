import plop.server.Plop

class BootStrap {

    def init = { servletContext ->
        println new Plop(text: 'YOLO!').save()
    }
    def destroy = {
    }
}
