import { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

// Configure standard API base URL
const API_URL = "http://localhost:8080/api/images";

function App() {
    const [file, setFile] = useState(null);
    const [images, setImages] = useState([]);
    const [uploading, setUploading] = useState(false);

    // Fetch images on component mount
    useEffect(() => {
        fetchImages();
    }, []);

    const fetchImages = async () => {
        try {
            const response = await axios.get(API_URL);
            setImages(response.data);
        } catch (error) {
            console.error("Error fetching images:", error);
        }
    };

    // File Selection
    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    // Form Submission
    const handleUpload = async (event) => {
        event.preventDefault();
        if (!file) return;

        setUploading(true);
        const formData = new FormData();
        formData.append("file", file);

        try {
            await axios.post(`${API_URL}/upload`, formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });
            // Refresh the list after successful upload
            await fetchImages();
            setFile(null); // Reset file input
            // Reset file input value manually via DOM if needed, or simple reset:
            event.target.reset();
        }
        catch (error) {
            console.error("Error uploading file:", error);
            alert("Upload failed!");
        }
        finally {
            setUploading(false);
        }
    };

    return (
        <div className="app-container">
            <h1>My Photo Gallery</h1>

            {/* Upload Section */}
            <div className="upload-section">
                <form onSubmit={handleUpload}>
                    <input type="file" onChange={handleFileChange} accept="image/*" />
                    <button type="submit" disabled={!file || uploading}>
                        {uploading ? "Uploading..." : "Upload"}
                    </button>
                </form>
            </div>

            {/* Gallery Grid */}
            <div className="image-grid">
                {images.map((img) => (
                    <div key={img.id} className="image-card">
                        <img src={img.url} alt={img.fileName} />
                        <p>{new Date(img.createdAt).toLocaleDateString()}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default App;