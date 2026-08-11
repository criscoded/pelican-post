export interface Image {
  id?: number;
  originalFileName: string;
  s3Key: string;
  contentType: string;
  url: string;
  note?: string;
  theme?: string;
}
