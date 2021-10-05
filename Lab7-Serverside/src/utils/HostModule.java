package utils;

abstract class HostModule {
    private boolean active;
    private Host host;

    public HostModule(Host host) {
        this.active = false;
        this.host = host;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changeActive() {
        this.active = !this.active;
    }

    public boolean isActive() {
        return this.active;
    }

    public Host getHost() {
        return this.host;
    }

}
