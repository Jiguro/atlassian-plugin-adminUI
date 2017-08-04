import { PluginNgPage } from './app.po';

describe('plugin-ng App', () => {
  let page: PluginNgPage;

  beforeEach(() => {
    page = new PluginNgPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
