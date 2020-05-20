export class NavigationLink {
  constructor(
    public label: string,
    public icon: string,
    public route: any[],
    public notificationNumber?: number,
    public notificationTooltip?: string
  ) {
    this.label = label;
    this.icon = icon;
    this.route = route;
    this.notificationNumber = notificationNumber;
    this.notificationTooltip = notificationTooltip;
  }
}
